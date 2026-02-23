# Quarkusコマンドラインインターフェースのインストール

[Quarkus コマンドラインインターフェイス (CLI) を使用した Quarkus アプリの構築](https://ja.quarkus.io/guides/cli-tooling)

WindowsのPowerShellでインストールするコマンド

```
iex "& { $(iwr https://ps.jbang.dev) } trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/"
iex "& { $(iwr https://ps.jbang.dev) } app install --fresh --force quarkus@quarkusio"
```

<!--
# getting-started

https://ja.quarkus.io/guides/getting-started

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.
-->

# git cloneとビルドの方法

## git clone

当ソースコードをgit cloneで取得する。

```
git clone https://github.com/jjugnsjeecditips202602/febtips04quarkusarc.git
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)


Hellow World
```
curl -w "\n" http://localhost:8080/rest4quarkusarc/hello
```

# 動作確認

ブラウザに次のURLを打ち込む

http://localhost:8080/rest4quarkusarc/beanlist

http://localhost:8080/rest4quarkusarc/memoryusage

http://localhost:8080/rest4quarkusarc/rqst

http://localhost:8080/rest4quarkusarc/dpndnt


# 参考情報

Quarkus ArCのdestroyは具体的に何をしているのか？調査できるならば、する方が良い。

https://github.com/quarkusio/quarkus/blob/main/independent-projects/arc/runtime/src/main/java/io/quarkus/arc/impl/InstanceImpl.java#L225

ArCのCreationalContextImplにもdependentInstancesというフィールドがある。

https://github.com/quarkusio/quarkus/blob/main/independent-projects/arc/runtime/src/main/java/io/quarkus/arc/impl/CreationalContextImpl.java

関連するクラスのソースコード

io.quarkus.arc.impl.ArcCDIProvider$ArcCDI

https://github.com/quarkusio/quarkus/blob/main/independent-projects/arc/runtime/src/main/java/io/quarkus/arc/impl/ArcCDIProvider.java#L34

io.quarkus.arc.impl.InstanceImpl

https://github.com/quarkusio/quarkus/blob/main/independent-projects/arc/runtime/src/main/java/io/quarkus/arc/impl/InstanceImpl.java

