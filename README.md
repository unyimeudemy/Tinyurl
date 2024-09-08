# Scalable URL shortening service

Implementation details of a scalable URL shortening service like [tinyurl.com,](https://www.tinyurl.com) including spam URL detection using the Bloom filter data structure. Built with Java, Spring Boot, MongoDB, and React.js.. Check out the live project [here](https://t-2t2q.onrender.com/) and the code [here](https://github.com/unyimeudemy/Tinyurl).

Here's a summary of how the service works: When a request to shorten a long URL comes in, we first check if it's a known spam URL using the Bloom filter data structure. If it's not spam, we generate a 64-bit ID using the Snowflake algorithm. This ID is then encoded with a base-58 alphanumeric system, in order to shorten it and enhance readability resulting in a string like 27qMi57J. We append this string to the service's base URL (frontend route) to create the short URL. Finally, we store the short and long URLs as a key-value pair in a MongoDB database. Please note that the service does not include deletion and updating of previously added URLs. These features are not part of the core service but would be necessary for a fully-fledged URL shortening service.

Now let's explore the reasons behind our implementation choices. First, I'd like to give a big shout-out to [educative.io](http://educative.io), as this project was inspired by their system design course. You should definitely check out their courses. Also, thanks to [@Austin Z. Henley](https://www.linkedin.com/in/austin-henley-a5b8a1118/) for the Bloom filter inspiration in this [article](https://austinhenley.com/blog/challengingalgorithms.html).

## Bloom filter data structure

This is the first place we check before doing anything. The idea here is that since our service completely hides what the original URL looks like, it can be used to mask spam URLs. An approach to counter this is by first having a list of known spam URLs and informing our service about all the URLs on this list. This way, if a request to shorten a spam URL is made in the future, it will easily flag it as spam and won't bother to shorten it.

Moreover, whatever method is used should not involve querying the spam URL table, as this would require traversing the entire table for each URL to be shortened, just to check if it is spam. Most annoyingly, a very high percentage of these requests to the spam URL database will return false, since there will be far fewer instances of someone attempting to shorten a spam URL compared to people shortening valid URLs. This would be a huge waste of computing resources, especially when the spam list is very long. A way to achieve this without visiting the database is to use the Bloom filter data structure.

To understand what a bloom filter is and how it works, forget about all the explanations out there and watch the video in the link below. Then come back to continue after subscribing to the channel (let us all support good work)

https://youtu.be/kfFacplFY4Y?si=DEZ6LcHbdfs6GCBp

Sincerely, I don’t think there is any written explanation that will beat the explanation in that video. But I will say that the Bloom filter data structure is an in-memory data structure used to determine membership in a set. It essentially answers the question: Is this item a member of a given group? For each answer from this algorithm, a "NO" is always 100% accurate, while a "YES" needs to be rechecked. This second behavior is known as false positive.

So how is this useful in our service? It allows us to completely avoid checking the spam URL database before shortening a long URL whenever we get a "NO" as an answer. However, if we get a "YES," we will need to check the spam URL database due to the algorithm’s false positive behavior.

You can read the article at the link below to see a detailed implementation of the algorithm and how to minimize the false positive issue.

## The short URL

When a URL shortening service is mentioned, most people probably think it's just about taking a long URL, hashing it with a function, and then storing it in a key-value pair database where the short URL is the key and the long URL is the value. However, this approach won't take you very far because as the number of URLs increases, so will the number of hash collisions. One way to solve this problem is to have an entirely independent system capable of generating a large number of unique IDs for an extended period of time.

This ensures that the possibility of a collision is slim to none. To achieve this, our short URL generation will occur in two phases. First, we generate a large number like 2468135791013 using a sequencer (Snowflake), and then we continually divide this number by 58 (since we intend to map it to a base-58 alphanumeric table) until we reach 1, keeping track of the remainder in each case. Finally, we map each remainder to its corresponding value in the base-58 alphanumeric table.

Snowflake algorithm (sequencer)

The goal here is to generate a unique ID, and while some people might have good opinions on a simple way to do this, let me first debunk the most popular and naïve opinions, which I, too, have been guilty of holding. 😜:

Using some sort of calculation, for example

```java
const min = 1000000
const max = 9999999999

const id = Math.floor(Math.random() * max) + min;
```
When we try to design a distributed, scalable system, the approach above will fall short in several ways. First, the code above does not generate unique IDs but random numbers, so sooner or later, there will be collisions where the same ID is generated more than once.

Secondly, unlike Snowflake, which generates time-ordered IDs, these random IDs are not ordered, making them unsuitable for sequencing operations. Finally, in a distributed system, generating random numbers on multiple nodes increases the likelihood of collisions, especially with a large number of requests. So imagine what would happen if you had the exact same code on different nodes generating the IDs.

Using the auto-incrementing integer feature of a database - first, auto-increment IDs are usually generated by a single database node, creating a bottleneck as the system scales, especially in distributed environments. Having different nodes of the database auto-generate their IDs will definitely lead to collisions. Then one might suggest, 'just make sure only one database generates the IDs.' Well, this would mean that we have a single point of failure. Additionally, we still face the problems of a lack of uniqueness and the generation of non-time-ordered IDs.

Using UUIDs (Universally Unique Identifiers) presents a significant issue: the generation of non-time-ordered IDs. Another less obvious concern is the size of a UUID. A typical UUID, like 67DB31FB-1887-4372-A8B0-E87C092D7D11, is 128 bits. For a service like tinyurl.com, which shortens around 100 million URLs per month (according to estimates), this means a substantial amount of storage is needed just for IDs. Additionally, indexing these large IDs in a database can be challenging.

Now that we have established the reason for using Snowflake, let's explain what the Snowflake algorithm is. The Snowflake algorithm is a distributed unique ID generator developed by Twitter. It creates 64-bit unique identifiers by combining a timestamp, data center ID, machine ID (worker ID), and a sequence number. The IDs are time-ordered, which is useful for sequencing events, and are generated without a central coordination point, allowing for high scalability. The idea of combining all these components to generate one ID guarantees uniqueness across the network. Even if two machines in the same network generate IDs at the exact millisecond, these IDs will be different due to the different machine IDs.

The article below gives a detailed explanation of the implementation of this algorithm.

## Encoder

After generating a unique ID with the Snowflake algorithm, we obtain a large base-10 number that can easily be converted into a base-64 short URL. However, the problem with a base-64 system is that it contains confusing characters, which can lead to readability issues. Characters like O (capital O) and 0 (zero), I (capital I) and l (lowercase L) can be easily confused, while characters like + and / should be avoided due to potential system-dependent encodings.

After removing all the confusing characters, we end up with a modified base-54 system.

To convert the base-10 number, we continually divide the base-10 number we obtained earlier until it becomes 0, and in each case, we record the remainder.

Base-10 = 2468135791013

2468135791013 % 58 = 17

42554065362 % 58 = 6

733690782 % 58 = 4

12649841 % 58 = 41

218100 % 58 = 20

3760 % 58 = 48

64 % 58 = 6

1 % 58 = 1

Note that these remainders will be used in reverse order, from 1 to 17. With this in mind, here is our list of remainders: [1, 6, 48, 20, 41, 4, 6, 17]. When each number is mapped to its respective value in the base-58 table, we get 27qMi57J. Finally, we append this to the base URL of the frontend page and send something like https://tiny.com/27qMi57J to the user.

## Database

Since the most frequent operations our service will handle are getting a long URL for a provided short URL (read-heavy operations), we will be using a NoSQL database like MongoDB. Another reason we are specifically using MongoDB and not another NoSQL database like Cassandra or Riak is that MongoDB uses the leader-follower protocol, while the others use the leaderless protocol.

This simply means that in MongoDB, only one node (leader) handles the write operations before other nodes replicate, making our service inherently protected from having duplicates since we are dealing with a service that has a high need for uniqueness. The other nodes (followers) are responsible for handling read operations, which is ideal since our service is read-heavy.

The leaderless protocol, used by other NoSQL databases, implies that any node can perform the write operations, and eventually, all nodes will reach consistency. However, if a read request comes in before consistency is reached, we might get outdated or inconsistent data, which could lead to issues like retrieving an incorrect long URL for a given short URL.

If you found this article helpful, you can follow me through the links below:

https://x.com/unyimeudoh22

www.linkedin.com/in/unyimeudoh