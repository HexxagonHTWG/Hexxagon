[![Scala CI](https://github.com/HexxagonHTWG/Hexxagon/actions/workflows/scala.yml/badge.svg?branch=master)](https://github.com/HexxagonHTWG/Hexxagon/actions/workflows/scala.yml)
[![codecov](https://codecov.io/gh/HexxagonHTWG/Hexxagon/branch/master/graph/badge.svg?token=GGXRQECCY7)](https://codecov.io/gh/HexxagonHTWG/Hexxagon)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Top Lang](https://img.shields.io/github/languages/top/HexxagonHTWG/Hexxagon?color=magenta)

██╗░░██╗███████╗██╗░░██╗██╗░░██╗░█████╗░░██████╗░░█████╗░███╗░░██╗
██║░░██║██╔════╝╚██╗██╔╝╚██╗██╔╝██╔══██╗██╔════╝░██╔══██╗████╗░██║
███████║█████╗░░░╚███╔╝░░╚███╔╝░███████║██║░░██╗░██║░░██║██╔██╗██║
██╔══██║██╔══╝░░░██╔██╗░░██╔██╗░██╔══██║██║░░╚██╗██║░░██║██║╚████║
██║░░██║███████╗██╔╝╚██╗██╔╝╚██╗██║░░██║╚██████╔╝╚█████╔╝██║░╚███║
╚═╝░░╚═╝╚══════╝╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝░╚═════╝░░╚════╝░╚═╝░░╚══╝

# Project

This project is developed as an example for the lecture Software Engineering at the HTWG Konstanz.

You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

## Hexxagon

Hexxagon is a strategy board game played in a 2 player mode. The goal is to capture as many fields as possible on a
hexagonal grid. The player with the most stones on the board wins.

You can place your stones by inputting a two-indexed location. All adjacent Fields with opposite stones become your own.

## Configurations

IntelliJ IDEA is recommended for development. You can import the project by opening the build.sbt file.
Each module has its own sbt file. You can run the project by selecting the main class and clicking the run button.

Testing is done with ScalaTest. You can run the tests by the predefined run configurations in `.test` or by
running `sbt test`.

### Pushing to master

Using `#major` or `#minor` in your commit message will trigger a corresponding version bump. Any commit to master will
trigger a new release.

# Contributors

![GitHub Contributors Image](https://contrib.rocks/image?repo=HexxagonHTWG/Hexxagon)

## GUI - Overview

![HEXXAGON](https://user-images.githubusercontent.com/34040518/151370057-dd08f6ff-a616-44c8-8bac-97bae45c1a9f.gif)
![grafik](https://user-images.githubusercontent.com/34040518/145568350-a6b652d0-cc23-46a6-b5c3-1ecc1d98556a.png)
![grafik](https://user-images.githubusercontent.com/34040518/147228936-72d1362f-46bd-4367-8f57-4067dd7dd758.png)
![grafik](https://user-images.githubusercontent.com/34040518/147615216-5e6b8078-cf84-41f2-9e3b-ac4b67ec902b.png)

## JOTD - Joke of the Day

![Jokes Card](https://readme-jokes.vercel.app/api)
