# Compression tool

<a name="readme-top"></a>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
        <a href="#built-with">Built With</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#development">Development</a></li>
        <li><a href="#docker">Docker</a></li>
        <li><a href="#examples">Examples</a></li>
      </ul>
    </li>
    <li><a href="#license">License</a></li>
    <li><a href="#contacts">Contacts</a></li>
  </ol>
</details>

### Built With

- [Java 21 temurin][java]
- [Maven][maven]
- [Docker][docker]
- [Picocli][picocli]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

This application allows you to to compress multiple files into a tar archive and
compress it using different algorithms.

> [!WARNING]
> Compressing binary files isn't properly supported by the current implementation
> of our algorithms and will most likely fail.

### Prerequisites

#### Java 21

- [asdf][asdf]

  ```sh
  # Install the plugin if needed
  asdf plugin add java
  # Install
  asdf install java latest:temurin-21
  ```

- Mac (homebrew)

  ```zsh
  brew tap homebrew/cask-versions
  brew install --cask temurin@21
  ```

- Windows (winget)

  ```ps
  winget install EclipseAdoptium.Temurin.21.JDK
  ```

#### Development

Use the maven wrapper to install dependencies, build and package the project.

```sh
# install the dependencies
./mvnw clean install
# build
./mvnw package
# run
java -jar target/dai-lab-01-1.0.jar --help
```

#### Docker

The application can be used with docker.

```sh
# Build
docker build -t compression-tool:latest .
# Example run to compress input_a.txt and input_b.txt into output.tar.rle
docker run --rm -v ".:/data" compress-tool:latest -c -a RLE -o /data/output.tar.rle /data/input_a.txt /data/input_b.txt
```

Or you can use docker compose

```sh
# Compress ./input.txt to ./output.tar.lzw
env ALGO="LZW" INPUT_DIR="./" OUTPUT_DIR="./" INPUT="input.txt" OUTPUT="output.tar.lzw" docker compose up --build compress
# Extract ./input.tar.lzw to ./output/
env ALGO="LZW" INPUT_DIR="./" OUTPUT_DIR="./" INPUT="input.txt" OUTPUT="output" docker compose up --build extract
```

#### Examples

```sh
# Compress multiple files using LZW algorithm
java -jar target/dai-lab-01-1.0.jar -c -a LZW -o myfile.tar.lzw README.md pom.xml

# Extract compressed files using LZW to a specific directory
java -jar target/dai-lab-01-1.0.jar -x -a LZW -o mydir myfile.tar.lzw
```

#### Github actions

If you want to test the `Github actions` on your machine, you can use [act](https://github.com/nektos/act).

Before you launch any workflow, make sure you have created a repository secret named `AUTH_TOKEN`.

Then, create a file named `.secrets` which should contain the following:

```env
AUTH_TOKEN=<YOUR_AUTH_TOKEN>
```

Finally, launch the publish workflow (which publishes the mvn package to Github repository) with the following command:

```sh
act --secret-file .secrets
```

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contacts

- [Thynkon](https://github.com/Thynkon)
- [Mondotosz](https://github.com/Mondotosz)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[java]: https://adoptium.net/temurin/releases/
[maven]: https://maven.apache.org/
[docker]: https://www.docker.com/
[picocli]: https://picocli.info/
[asdf]: https://asdf-vm.com/
