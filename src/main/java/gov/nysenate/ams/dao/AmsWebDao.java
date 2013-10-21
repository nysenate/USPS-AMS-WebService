package gov.nysenate.ams.dao;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.CityStateResult;
import gov.nysenate.ams.util.Application;
import gov.nysenate.util.Config;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.client.fluent.Content;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * USPS adapter used for performing address validations.
 *
 * The USPS Address Information API is currently only capable of sending
 * and receiving XML responses and requests. The overall format of the
 * request body is as follows:
 *
 * <XYZRequest USERID="xxxx">
 *     <Address ID="0">
 *        <FirmName></FirmName>
 *        <Address1></Address1>
 *        <Address2></Address2>
 *        <City></City>
 *        <State></State>
 *        <Zip5></Zip5>
 *        <Zip4></Zip4>
 *     </Address>
 * </XYZRequest>
 *
 * The convention for the request is that Address1 refers to the apartment
 * or suite number and Address2 refers to the street address. FirmName can
 * be thought of as the addressee line.
 *
 * In order to keep the rest of the codebase from having to deal with this
 * supply the Address model with the street address set to addr1. addr2 of
 * the supplied model if set will simply be concatenated onto addr1 of the
 * model.
 *
 * The AddressResult object that the methods return will contain a single
 * Address object. The addr1 field will contain the fully validated street
 * address. The addr2 field will always be empty. If the request failed
 * then the address object in the AddressResult will be null, isValidated
 * will be false, and the error messages will be stored in the messages array.
 *
 * It is important to note that this class is not thread-safe so the
 * calling method must ensure that multiple threads do not operate on
 * the same instance of this class.
 *
 * Refer to the online documentation (link subject to change)
 * https://www.usps.com/webtools/_pdf/Address-Information-v3-1b.pdf
 */
public class AmsWebDao
{

}
