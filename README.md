# Functional Iterables

[![Build Status](https://travis-ci.org/smaspe/FunctionalIterables.svg?branch=master)](https://travis-ci.org/smaspe/FunctionalIterables) [![codecov.io](https://codecov.io/github/smaspe/FunctionalIterables/coverage.svg?branch=master)](https://codecov.io/github/smaspe/FunctionalIterables?branch=master)

A bunch of Iterable functions for more fluent functional programming, for Java 7

Because other solutions are too big and complex, this fit in a single file and does a limited number of things.

# Download

[![Download](https://api.bintray.com/packages/smaspe/Default/FunctionalIterables/images/download.svg) ](https://bintray.com/smaspe/Default/FunctionalIterables/_latestVersion)

## Maven

    <dependency>
      <groupId>com.github.smaspe</groupId>
      <artifactId>iterables</artifactId>
      <version>0.3.0</version>
    </dependency>

## Gradle

    compile group:'com.github.smaspe', name:'iterables', version: '0.3.0'

# Basics

```java
List<Integer> squaresOfEven = FuncIter.iter("1", "2", "3", "4")
        .map(Integer::valueOf)
        .filter((i) -> i % 2 == 0)
        .map((i) -> i * i)
        .collect();
// [4, 16]
```

## `FuncIter`
`FuncIter` is the base class. `_` was nice, but java complains and threatens to stop supporting `_` as a class name.

It is an `Iterable`. Functions are usually provided as instance functions and static functions which take an `Iterable` as first parameter. For example, `public <R> FuncIter<R> map(Func<T, R> func);` is available as `public static <R> FuncIter<R> map(Iterable<T> input, Func<T, R> func);`

## Functions
Functions are divided in 3 groups:
- Creators, to create instances of `FuncIter` from `Iterable`, arrays, single values, lists of iterables...
- Functions, to manipulate things
- Collectors, to retrieve the content of the `Iterable` in another format (typically a `List`. Actually an `ArrayList`. There are discussion as to whether a function should declare `List` or `ArrayList`. I prefer `ArrayList`. If you want to know why, ask me.)

## Other
A few extra things that are packaged with:
- `Pair<U,V>` a simple class that holds 2 values
- `Func<T,R>` a simple functional interface with a method that has 1 parameter and returns something
- `Exec<T>` a simple functional interface with a method that has 1 parameter and returns void

# Usage

## Create an instance
- `iter` - From a vararg
- `from` - From an existing `Iterable`
- `chain` - From several `Iterable`s (an `Iterable` of `Iterable`s, or a vararg)
- `repeat` - Infinitely repeats a single value
- `range` - Counts

## Functional stuff that return more iterables
- `map` - Transforms `T` to `R` according to the mapping functions
- `filter` - Only returns the `T` that match the predicate
- `zip` - Combine `T1` and `T2` into `Pair<T1, T2>`

## Functional stuff that return single values
- `any` - Stops as soon as a value matches the predicate
- `all` - Stops as soon as a value does not match the predicate
- `firstOr` - Self-explanatory
- `each`
- **`reduce`** - Not implemented yet

## Collecting
- `collect` - Gives you an `ArrayList`. Don't try to `collect` `repeat`.

## TODO
- [x] Actually work with java 7 and retrolambda
- [x] Publish on maven central, add the gradle line here
- [ ] Create the basic functions.
    - [x] wrappers and creators,
    - [x] chain,
    - [x] map,
    - [x] filter,
    - [x] find,
    - [ ] reduce,
    - [ ] max (-> reduce + Comparator),
    - [ ] sum (longs and doubles?) (-> reduce),
    - [x] zip,
    - [x] all,
    - [x] any,
    - [x] range
- [x] Publish on maven central, add the gradle line here
- [ ] `until` function to `collect`
- [ ] `count` param to `repeat`
- [ ] Or, `until` function on `FuncIter` (with opt. inclusive flag), and `firstN` function on `FuncIter` as well
- [ ] Currying helpers

# Licence

Copyright 2015 Simon Maspero

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
