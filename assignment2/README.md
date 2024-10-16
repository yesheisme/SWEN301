## Notes for Marker

No special instructions.

### Why I chose json.org as my JSON library

##### Experience:
* In my very limited experience I've found json.org to be a simple and reliable tool for basic JSON tasks. It’s great for handling JSON in Java without adding extra complexity or slowing things down.

##### Documentation:
* The documentation for json.org is straightforward and easy to understand, making it suitable for "devs" like me who need a simple tool to get JSON parsing tasks done efficiently. 

##### Technical Aspects:
* Performance: json.org is one of the fastest for basic JSON parsing tasks. However it may not outperform libraries optimized for large-scale or complex JSON operations (like Gson or Fastjson).

* Stability: The library is stable, as it has been around for a long time without significant changes. Its lack of regular updates makes it a reliable and consistent choice for developers.

* Dependencies: One of json.org's most significant advantages is its lack of external dependencies.

##### Social Aspects:
* Community: json.org doesn't have as active a community as newer libraries like Gson or Fastjson. But as stated previously because of it's great stability it doesn't neccasarily need a super active community. 

* License: json.org is licensed under the "MIT License," which allows for free usage in both commercial and non-commercial projects.

* Usage by others: This library is widely used in many foundational systems. Although other libraries have overtaken it in popularity, it's still frequently found in legacy systems and low-dependency projects.


| Feature  | json.org | Gson     | Fastjson |
|----------|----------|----------|----------|
| Pros     | Lightweight, with no external dependencies.    | Rich features.    | Extremely fast JSON parsing and serialization.    |
|          | Stable and battle-tested.    | Strong Google support and active development.    | Good support for complex JSON operations and large datasets.    |
|          | Simple and easy to use for basic tasks.    | Extensive documentation and community support.    | Offers advanced features such as auto-detection of circular references.    |
| Cons     | Limited features compared to modern libraries.    | Slightly larger size due to extra features.    | Potential security vulnerabilities in the past.    |
|          | No active community development or feature additions.    | Slower performance for large-scale data compared to Fastjson.    | Higher complexity and steeper learning curve for simple tasks.    |
| Performance        | Fast for simple tasks.    | Good for complex tasks but not the fastest.    | Excellent for both simple and complex tasks.    |
| Dependencies       | No external dependencies.    | Minimal (but heavier than json.org).    | Heavier than json.org and Gson.    |
| License            | MIT    | Apache 2.0    | Apache 2.0    |
| Community Support  | Small, stable community.    | Large community.    | Very active community.    |


Finally I chose json.org because it's great for situations where you need something simple, lightweight, and reliable. For bigger projects with more complex JSON tasks or large amounts of data, Gson or Fastjson might be better because they have more features and perform faster. But for smaller projects, json.org is great.



## Rules 

* violating of name rules
* violating the maven standard project layout 
* use of absolute references 
* references to local libraries 
* use of libraries that are not whitelisted
* commiting binaries or IDE meta data to the repository

## Whitelisted libraries 

* json.org
* gson
* jackson
* fastjson
* jsonp

## Maven standard project layout
```
my-project/ 
│ 
├── src/ 
│ ├── main/ 
│ │ ├── java/ # Application/production Java source code 
│ │ ├── resources/ # Application/production resources (e.g.,XML, properties files) 
│ │
│ ├── test/ 
│ │ ├── java/ # Test Java source code 
│ │ ├── resources/ # Test resources (e.g., configuration for testing) 
│ │
│ ├── target/ # Compiled bytecode and build artifact(generated) 
│ │
│ ├── pom.xml # Project Object Model (POM) configuration file


