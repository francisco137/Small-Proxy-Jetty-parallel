# Small Proxy Jetty - parallel

## Description

The aim of the program *Small Proxy Jetty - parallel* is to give statistics of the contributions amount of developers who
contribute their work into GitHub repositories. In fact the program has only one function with only one free parameter.

Suffix *parallel* means that data quering through unknown number of pages is performed
by few collectors in parallel. Twin program *Small Proxy Jetty - recursive* do it
using one collector only going throug the pages recursively.

The program accept browser request in the form:

```
http://localhost:8081/org/:organization/contributors
```

where *`:organization`* is this free parameter and means the name of an organization in GitHub. For instance
when you input as an organization name the `wesabe` name:

```
http://localhost:8081/org/wesabe/contributors
```

you should obtain the list of developers and their contributions in the following form

```
{
  "jplang" : 1557,
  "eventualbuddha" : 718,
  "codahale" : 464,
  "alexeyv" : 343,
  "stellsmi" : 137,
  ...
  ...
  ...
  "sirianni" : 1,
  "vandersonmota" : 1,
  "jkrall" : 1,
  "capoferro" : 1,
  "offbyone" : 1
}
```

Records are sorting by contributions amount descending, and the amounts are total contributions of each developer in all repositories of the
organization.


## Build & Run

The program Small Proxy runs through SBT and to build and run (assuming the build went smoothly)
it is enough to perform:

```sh
$ cd /to/the/path/when/program/is/installed
$ ./start.sh
```

After successfully starting you should see on the console the last lines like here:
```
2020-03-14 21:34:01.366:INFO:oejs.AbstractConnector:main: Started ServerConnector@5386659f{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
2020-03-14 21:34:01.367:INFO:oejs.Server:main: Started @3226ms
```

Please notice, that there is written port where we should point the browser, in this case:
[http://localhost:8081/](http://localhost:8081/).

If you want different port change in the file `build.sbt` the port number in the line:
```
containerPort in Jetty := 8081
```
and restart the server.



## Instruction - how to use the program

1. Open your browser

2. Input in the address bar request in the form:

```
http://localhost:8081/org/:organization/contributors
```
where the word `:organization` replace by GitHub organization name.

3. Press *ENTER* or click the execution arrow.

## Authorization

In GitHub the access to some organizations is protected. Program has the possibility to introduce GitHub token
as an authorization mechanism.

To make the authorization export environment variable named `GH_TOKEN` in the session where server is going to be started, like:

```
export GH_TOKEN="My_secret_token"
```
and run server.

