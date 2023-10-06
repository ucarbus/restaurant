## Restaurant Opening Hours Assignment Documentation

### Introduction

Before starting the assignment, my first goal was to lower the representational gap between the problem and code, so I 
use classes like Day, Restaurant Availability which we also have in real life.

Then, I focus on how we actually do the translation from a given json file into a more human-readable format. Then, I notice that
there are always repeating steps that we need to perform in order to have more readable output. That gives me the feeling that 
these steps are like the rules, and these rules should be executed in a specific order, and we can obtain the result in the end.
This can be represented as Rule Engine in the code. The Rule Engine consists of different parts that work together to process the given input data.
The Rule Engine follows a chain of responsibility pattern, where each rule checks a specific condition and passes the request to the next rule in the chain if necessary. This allows for a modular and extensible approach to handle various edge cases.

### Suggestions for Part 2
The problem is still can be described with the given input file, but it might be harder to understand and deal with, especially when the opening hours cover two days.

There are different ways to present the information that could be easier for people to read and work with.

One option is to show the opening hours in a more clear format, where each day is specified along with the opening and closing times:

```json
{
    "monday": [
        {
            "open": "32400",
            "close": "39600"
        },
        {
            "open": "57600",
            "close": "82800"
        }
    ],
    ...
}
```

Here's an alternative way to represent the information. Instead of the previous formats, we can use an array called "openingPeriods" where each period has a start day and time, as well as an end day and time. This format makes it clear when the opening period spans across two days:

```json
{
    "openingPeriods": [
        {
            "start": "Monday 9:00 AM",
            "end": "Monday 8:00 PM"
        },
        {
            "start": "Monday 10:00 PM",
            "end": "Tuesday 2:00 AM"
        },
        ...
    ]
}
```

In this format, we specify the start and end times for each opening period, using specific days and times. For example, "Monday 9:00 AM" or "Tuesday 2:00 AM". This makes it easier to understand when the opening hours extend beyond a single day. However, this approach also loses the ability to work independently of timezones and would require manual handling of timezones.

To sum up, the choice of data structure depends on the specific needs and limitations of the application. The best choice will vary depending on the specific requirements and context.

### Conclusion

In conclusion, the Restaurant Opening Hours Assignment aims to parse less human-readable input, converts it to more human-readable output, and expose this data via REST endpoint. 
