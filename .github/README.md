# Memorized

---

### 🏗️ This project is still in development.

This project is still in development, it is not yet possible to use it. The project is subject to significant change in
the future.

## 📝 Summary

* [Goal of Memorized server](https://github.com/Darkkraft/Memorized#-goal-of-memorized-server)
* [When to use?](https://github.com/Darkkraft/Memorized#-when-to-use)
* [Features](https://github.com/Darkkraft/Memorized#-features)
* [How to use?](https://github.com/Darkkraft/Memorized#%EF%B8%8F-how-to-use)
* [Examples](https://github.com/Darkkraft/Memorized#%EF%B8%8F-examples)
* [Requirements](https://github.com/Darkkraft/Memorized#%EF%B8%8F-requirements)

## ❓ Goal of Memorized server

**Memorized** is a standalone cache server written in **Java**. Its goal is to create a really lightweight and efficient
cache server with high **flexibility**.
This project does not use a library to manage connections between server and client, but instead uses a NIO server.

## 🔍 When to use?

This is **not** a cache server that can be used in all cases. The aim is to provide a cache server that is **flexible**
enough to automate certain actions and best meet sometimes complex system requirements.
Typically, actions are **triggered** when an element is modified.
Some important aspects of this project are:

* Great flexibility, making it easy to add your own elements to suit your needs.
* Maintain maximum consistency between clients.
* Be able to perform atomic operations on any object.
* Have the smallest possible memory and CPU footprint.
* Keep access to the project open so that anyone can use it.
* Easy and lightweight implementation on native applications and all types of environments (mainly Java).

## ✨ Features

Here is a list of current features:

* Custom authentication
* Customizable codec system
* Multithreaded client connection management
* Distributed collections
* Custom distributed objects
* And many more!

## 🛠️ How to use?

This section is not available yet. It will be divided into several sections in the Wiki tab.

## ✍️ Examples

See full
examples [here](https://github.com/Darkkraft/Memorized/tree/master/examples/src/main/java/be/darkkraft/memorized/example).

#### Start a Memorized client

```java
public class MyClient {

    public static void main(final String[] args) {
        final MemorizedClient client =
                new MemorizedClientBuilder().serverAddress(new InetSocketAddress("127.0.0.1", 12345))
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