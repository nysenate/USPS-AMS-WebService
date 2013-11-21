/*
** Project: USPS-AMS-WebService
** Author: Ken Zalewski
** Organization: New York State Senate
*/

#include <stdio.h>		/* fprintf(), perror() */
#include <stdlib.h>		/* exit() */
#include <unistd.h>		/* close(), fork() */
#include <sys/socket.h>		/* socket() */
#include <netinet/in.h>		/* htons() */
#include <arpa/inet.h>		/* inet_ntoa() */

#include "tcp.h"


static int create_listener(int port);
static int wait_for_connection(int listenfd);
static int read_incoming_data(int connfd);


static int
create_listener(int p_portnum)
{
  int listenfd;
  struct sockaddr_in sin;
  uint16_t portnum = (uint16_t)DEFAULT_PORT;

  listenfd = socket(AF_INET, SOCK_STREAM, 0);
  if (listenfd < 0) {
    perror("socket");
    fprintf(stderr, "Error: Unable to create listener socket\n");
    return -1;
  }

  if (p_portnum != 0) {
    portnum = p_portnum;
  }

  sin.sin_family = AF_INET;
  sin.sin_addr.s_addr = htonl(INADDR_ANY);
  sin.sin_port = htons(portnum);

  if (bind(listenfd, (struct sockaddr*)&sin, sizeof(sin)) < 0) {
    perror("bind");
    fprintf(stderr, "Error: Unable to bind socket fd %d to port %d\n",
                    listenfd, portnum);
    close(listenfd);
    return -1;
  }

  if (listen(listenfd, 16) < 0) {
    perror("listen");
    fprintf(stderr, "Error: Unable to listen on fd %d\n", listenfd);
    close(listenfd);
    return -1;
  }

  return listenfd;
} /* create_listener() */


static int
wait_for_connection(int listenfd)
{
  struct sockaddr_in sin;
  socklen_t slen = sizeof(sin);

  int acceptfd = accept(listenfd, (struct sockaddr*)&sin, &slen);

  if (acceptfd < 0) {
    perror("accept");
    fprintf(stderr, "Error: Unable to accept connection on fd %d\n", listenfd);
    return -1;
  }

  fprintf(stderr, "Notice: Received connection from remote IP %s on fd %d\n",
                  inet_ntoa(sin.sin_addr), acceptfd);
  return acceptfd;
} /* wait_for_connection() */


static int
read_incoming_data(int connfd)
{
  char buf[DEFAULT_BUFSIZE];
  int nbytes = recv(connfd, buf, DEFAULT_BUFSIZE, 0);
  if (nbytes <= 6) {
    fprintf(stderr, "Error: Invalid protocol header\n");
    return -1;
  }
  
  fprintf(stderr, "Got [%s]\n", buf);
  return 0;
} /* read_incoming_data() */


int
handle_connections(int portnum)
{
  int listenfd;
  int acceptfd;
  pid_t childpid;

  listenfd = create_listener(portnum);
  if (listenfd < 0) {
    fprintf(stderr, "Warning: Exiting main network event loop\n");
    return -1;
  }

  while (1) {
    fprintf(stderr, "Notice: Waiting for a connection...\n");
    acceptfd = wait_for_connection(listenfd);
    if (acceptfd < 0) {
      fprintf(stderr, "Warning: Exiting main network event loop\n");
      return -1;
    }

    childpid = fork();

    if (childpid > 0) {
      close(acceptfd);
      fprintf(stderr, "Notice: Child process id=%d forked to handle incoming connection\n", childpid);
    }
    else if (childpid == 0) {
      close(listenfd);
      fprintf(stderr, "Notice: Processing request in child process\n");
      read_incoming_data(acceptfd);
      exit(0);
    }
    else {
      perror("fork");
      fprintf(stderr, "Error: Unable to create child process to handle new connection\n");
    }
  }
} /* handle_connections() */
