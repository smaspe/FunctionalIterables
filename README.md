[ ![Download](https://api.bintray.com/packages/smaspe/Default/FunctionalIterables/images/download.svg) ](https://bintray.com/smaspe/Default/FunctionalIterables/_latestVersion)

# Iterables
A bunch of Iterable functions for more fluent functionnal programming, for Java 7

Because other solutions are too big and complex, this fit in a single file and does a limited number of things.

# Basics

## `_`
`_` is the base class. It is short, and also I am not good with names. It is an `Iterable`. Functions are usually provided as instance functions and static functions which take an `Iterable` as first parameter. For example, `public <R> _<R> map(Func<T, R> func);` is available as `public static <R> _<R> map(Iterable<T> input, Func<T, R> func);`

## Functions
Functions are devided in 3 groups:
- Creators, to create instances of `_` from `Iterable`, arrays, single values, lists of iterables...
- Functions, to manipulate things
- Collectors, to retrieve the content of the `Iterable` in another format (typically a `List`. Actually an `ArrayList`. There are discussion as to whether a function should declare `List` or `ArrayList`. I prefer `ArrayList`. If you want to know why, ask me.)

## Other
A few extra things that are packaged with:
- `Pair<U,V>` a simple class that holds 2 values
- `Func<T,R>` a simple functional interface with a method that has 1 parameter and returns something

# Usage

## Create an instance
- `iter`
- `from`
- `repeat`
- `chain`

## Functional stuff that return more iterables
- `map`
- `filter`
- `zip`
- Add stuff here

## Functional stuff that return single values
- `any`
- `all`
- TODO
- `sum` or `reduce` or both
- `find` (wrap around filter...)

## Collecting
- `collect`. Gives you an `ArrayList`. Don't try to `collect` `repeat`.

## TODO
- Actually work with java 7 and retrolambda
- Create the basic functions. ~~iter(array)~~, ~~chain~~, flatten, ~~map~~, ~~filter~~, ~~find~~, reduce, max, sum (longs and doubles?), ~~zip~~, all?, any?, range? (This start to look a lot like python builtins)
- Publish on maven central, add the gradle line here
- `until` function to `collect`
- `count` param to `repeat`
- Currying helpers
- Similar helpers with Maps (possibly just collecting an `Iterable<Map.Entry<K, V>>` into a `Map`, the rest should be transparent)
