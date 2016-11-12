# workload

# Entities

User
 - id (could be a uuid or email)

Event
 - type (enum: in/out)
 - datetime (datetime)
 - user_ref (user.id)
 
Workday
 - date (date)
 - work time (minutes)
 - pause times (minutes)
 
Workweek
 - calender week
 - work time (minutes)
 - pause time (minutes)
 - list of workdays
 
 # API endpoints
POST /events
GET  /events
 
GET /workloads/{year}/{weekOfYear}/{dayOfWeek}

# Local Development
- Create service credentials for AppEngine from Google Cloud Console (JSON) and put it to ${project_root}/backend/workload-service-auth.json
- change to ${project_root}/backend
- execute command `gcloud beta emulators datastore start` to start the datastore emulator
- start the spring backend application adding system property `spring.profiles.active=development`
