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
