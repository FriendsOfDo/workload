[![Build Status](https://travis-ci.org/FriendsOfDo/workload.svg?branch=master)](https://travis-ci.org/FriendsOfDo/workload)

# workload

# Entities / Resources
### Event
```
id       long           The ID uniquely identifying the event.
userId   String         The ID of the user owning the event.
type     Type (enum)    The type of the event. Can either be IN or OUT.
lat      double         The latitude part of the geo coordinate where the event occurred.
lon      double         The longitude part of the geo coordinate where the event occurred.
date     Date           The date/time when the event occurred.
```

### User
```
id      String     The ID uniquely identifying the user. Could e.g. be an email or UUID.
```

### WorkDay
```
date           LocalDate      The date of the workday.
workingTime    int            Minutes the user worked at this workday.
pauseTime      int            Minutes the user paused his/her working time (e.g. lunch times).
events         List<Event>    The list of events that occurred at this workday.
```
 
### Workweek
```
year           int              The year.
calendarWeek   int              The calendar week of the given year.
workingTime    int              Minutes the user worked in this work week.
pauseTime      int              Minutes the user paused his/her work in this work week.
workDays       List<WorkDay>    The list of workdays in this week.
```

### Status
```
atWork     boolean     Determines if the user is currently working or pausing.
```

### Workplace
```
id           The unique ID identifying the workplace.
userId       The ID of the user the workplace belongs to.
name         A user defined name of the workplace (e.g. in case the user works in different workplaces).
street       The street where the workplace is located in.
city         The city where the workplace is located in.
postcode     The postcode of the city where the workplace is located in.
lat          The latitude part of the geo coordinate where the workplace is located.
lon          The longitude part of the geo coordinate where the workplace is located.
```

# API endpoints
## Events
`POST    /{userId}/events`

`GET     /{userId}/events`

`GET     /{userId}/events/{id}`

`DELETE  /{userId}/events/{id}`

## Workloads
`GET     /{userId}/workloads/{year}/{weekOfYear}`

## Workplaces
`POST    /{userId}/workplaces`

`GET     /{userId}/workplaces`

`DELETE  /{userId}/workplaces/{id}`

## Status
`GET     /{userId}/status`


# Local Development
- Create service credentials for AppEngine from Google Cloud Console (JSON) and put it to ${project_root}/backend/workload-service-auth.json
- change to ${project_root}/backend
- execute command `gcloud beta emulators datastore start` to start the datastore emulator
- start the spring backend application adding system property `spring.profiles.active=development`

# Deployments
- `cd ${project_root}/backend`
- `./gradlew build`
- `gcloud app deploy -v ${version}`
