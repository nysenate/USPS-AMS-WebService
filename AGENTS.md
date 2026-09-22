# AGENTS.md

This file provides guidance to coding agents working with code in this repository.

## Project overview

A WAR-packaged Java web service that exposes the closed-source USPS Address Matching System (AMS) C library through a JSON / XML / JSONP HTTP API. Java calls the AMS library via JNI through a thin C shim built in this repo. Targets Java 21, Tomcat 11, Jakarta Servlet 6.1.

## Build, run, deploy

The build has two halves, and both must succeed:

1. Native shim (`src/main/c/`):
   - `cd src/main/c && make` produces `libamsnative.so`. It needs `JAVA_HOME` set, the USPS AMS headers and shared objects in `/opt/usps_ams`, and links with `-lz4lnx64`.
   - `make install` copies the `.so` to `/usr/lib64`. Run `sudo ldconfig` afterward. Tomcat must be able to `dlopen` it from `java.library.path`.
   - `/etc/ld.so.conf` must list `/opt/usps_ams` so the USPS libraries resolve at runtime.
   - AMS data folders (`ams_comm`, `ams_dpv`, `ams_elot`, `lacslink`, `suitelink`) go in `/data/usps_ams` by default.

2. Java WAR:
   - `mvn compile war:war` → `target/usps-ams##<version>.war`. Deploy it to Tomcat.
   - `maven-war-plugin` copies `src/main/java` sources into `WEB-INF/classes` alongside the compiled classes on purpose.
   - `libamsnative.so` is loaded once per JVM and never unloaded, so **restart Tomcat when you redeploy**. Don't hot-redeploy.

There is no automated test suite and no CI. For ad-hoc checks:
- `gov.nysenate.ams.script.AmsTest` is a standalone `main` that smoke-tests the JNI bridge. Run it with `-Djava.library.path=<dir containing libamsnative.so>` and a populated `app.properties` on the classpath.
- `src/test/c/` builds `ams` (`make`), a standalone C program that uses the AMS library directly with no JNI. It reads `ams.cfg`. Use it to tell whether a problem comes from the USPS library or from the Java/JNI layer.

`src/main/php/EPF/` holds a separate PHP script that downloads AMS data updates from the USPS EPF service. It is not part of the WAR. Its `readme.md` has the details.

## Configuration

Runtime config is `src/main/resources/app.properties`; start from the `app.example.properties` template. `Application.bootstrap()` loads it once when the servlet context starts. If any `ams.cfg.*.path` value is blank, bootstrap fails. `shared.library.name` selects the native library name (default `amsnative`). `log4j2.xml.example` is the logging template.

## Architecture

**Bootstrap.** `Application` is a `@WebListener` that the container discovers automatically. On context init it builds an `AmsSettings` record from the config and creates the single `AmsNativeProvider`, then stores itself in a servlet-context attribute. Non-servlet callers such as `AmsTest` can instantiate it and call `bootstrap()` / `shutdown()` directly. `AmsNativeProvider.load()` calls `System.loadLibrary` once per JVM, guarded by a static flag, and then the JNI `setupAmsLibrary`.

**Request path.** The servlets for `/api/validate/*`, `/api/inquiry/*`, and `/api/citystate/*` each extend `BaseApiController<InputType>`. GET takes a single input from query params. POST takes a JSON array and returns a batch response. Controllers never write to the response. They store a response object with `ApiFilter.setApiResponse(...)`. `ApiFilter` is mapped to `/api/*` and calls `chain.doFilter` first. Afterward it serializes the stored object as JSON, XML, or JSONP, depending on the `format` query parameter. JSONP uses the `callback` parameter.

**JNI bridge.** `AmsNativeDao` declares the `native` methods, and `src/main/c/AmsNativeDao.c` / `.h` implement them. The query methods are `synchronized` because the USPS AMS library is not thread-safe. The rest of the code goes through `AmsNativeProvider`; keep `AmsNativeDao` limited to JNI signatures.

**Models vs. views.** JNI populates the domain objects in `gov.nysenate.ams.model`. The wire-format DTOs in `gov.nysenate.ams.client.response` and `gov.nysenate.ams.client.view` are built by controllers from the models and serialized by Jackson. Don't mix the two layers.

## Things to know before changing code

- Native method signatures in `AmsNativeDao.java` must match `src/main/c/AmsNativeDao.c`. If you rename a Java method, package, or argument type, you must regenerate the JNI header and rebuild the `.so`. The C side also finds model classes, constructor signatures, and accessor methods (on `Address`, `AmsSettings`, and others) by name. If you rename or change the signature of one of these, JNI breaks at runtime and the compiler won't catch it.
- To add an endpoint, subclass `BaseApiController` and register the servlet in `src/main/webapp/WEB-INF/web.xml`. Servlet mappings don't use annotations here.
- USPS AMS data expires. `AmsNativeProvider.getDataExpireDays()` exposes the remaining days.
- Jackson is version 3 (`tools.jackson.*`); `com.fasterxml.jackson.*` imports are wrong here. The servlet API is `jakarta.servlet.*`, not `javax.servlet.*`.
- `src/main/webapp/docs/` is Sphinx HTML output built from `docs/index.rst`. `docs/Makefile` writes to `docs/_build`, so after changing the `.rst` file you have to copy the rebuilt HTML over by hand.
