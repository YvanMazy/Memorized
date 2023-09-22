# Memorized

---

### 🏗️ This project is still in development.

This project is still in development, it is not yet possible to use it. The project is subject to significant change in
the future.

## ❓ Goal of Memorized server

**Memorized** is a standalone cache server written in **Java**. Its goal is to create a really lightweight and efficient
cache server with high **flexibility**.
This project does not use a library to manage connections between server and client, but instead uses a NIO server.

## ✨ Features

Here is a list of current features:

* Custom authentication
* Customizable codec system
* Multithreaded client connection management
* Distributed collections
* Custom distributed objects
* And many more!

## ✍️ Examples

See full
examples [here](https://github.com/Darkkraft/Memorized/tree/master/examples/src/main/java/be/darkkraft/memorized/example).

#### Start a Memorized client

```java
public class MyClient {

    public static void main(final String[] args) {
        final MemorizedClient client = new MemorizedClientBuilder().serverAddress(new InetSocketAddress("127.0.0.1", 12345))
                .authenticationInput(new UnsecureAuthenticationInput())
                .codecRegistry(new DefaultCodecRegistry().registerDefaults())
                .keyRegistry(new ClassKeyRegistry())
                .build();
        client.start();
    }
}
```

#### Start a Memorized server

```java
public class MyServer {

    public static void main(final String[] args) {
        final MemorizedServer server = new MemorizedServerBuilder().address(new InetSocketAddress("127.0.0.1", 12345))
                .workerThreads(1)
                .authenticator(new UnsecureAuthenticator())
                .codecRegistry(new DefaultCodecRegistry().registerDefaults())
                .dataRepositoryCoordinator(new DataRepositoryCoordinator())
                .build();
        server.start();
    }
}
```

## ⚙️ Requirements

* Java 17 or higher