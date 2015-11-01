import atexit
import time
import json
import urllib2
import datetime
from apscheduler.scheduler import Scheduler
from flask import Flask
import MySQLdb
import random

app = Flask(__name__)

cron = Scheduler(daemon=True)
# Explicitly kick off the background thread
cron.start()

@cron.interval_schedule(minutes=10)
def job_function():
    db = connectBinDb()
      
    for i in range(1,51):
    
        ts = time.time()
        st = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
          
        cursor = db.cursor()
        
        sqlQuery = "select f.filllevel,f.TimeStamp,s.BinId from smartbin s inner join smartbinfilllevel f  on f.BinId = s.BinId where s.BinId='"+i+"' AND f.isValid = 1"
        try:
            cursor.execute(sqlQuery)
            results = cursor.fetchall()
        except:
            print "Error: Unable to fetch data"
            
        for rows in results:
            validFillLevel = rows[0]
        
        percent = 0
        if (validFillLevel > 90):
            percent = random.randint(0,10)
        else:
            finalFillLevel = validFillLevel+20
        
            if (finalFillLevel > 100):
                finalFillLevel = 100
            
            percent = random.randint(validFillLevel,finalFillLevel)
        
        if (validFillLevel == percent):
            continue
            
        sqlQuery = "UPDATE `smartbin_db1`.`smartbinfilllevel` SET `isValid` = '0' WHERE `smartbinfilllevel`.`BinId` = "+str(i)+"; "
        try:
            print sqlQuery
            cursor.execute(sqlQuery)
            db.commit()
        except:
            print "Error: Unable to update data"
            db.rollback()
        
        sqlQuery = "INSERT INTO `smartbin_db1`.`smartbinfilllevel` (`BinId`, `FillLevel`, `TimeStamp`,`isValid` ) VALUES ('"+str(i)+"', '"+str(percent)+"', '"+str(st)+"', 1)"
        print sqlQuery
        try:
            cursor.execute(sqlQuery)
            db.commit()
        except:
            print "Error: Unable to insert data"
            db.rollback()
  
    # Populate for bin 52 from thingspeak
    url = "https://thingspeak.com/channels/56157/feed.json"
    response = urllib2.urlopen(url)
    data = response.read()
    print data
    
    jsonContent = json.loads(data)

    jsonFeeds =  jsonContent["feeds"]

    capacity = 504
    
    for entry in jsonFeeds:
        fillLevel = entry.get('field1')
        if (int(fillLevel) < 0):
                continue
    
        timeStampFmt = time.strptime(str(entry.get('created_at')), "%Y-%m-%dT%H:%M:%SZ") 
        if (int(timeStampFmt.tm_hour)/10 == 0):
            hour = "0"+str(timeStampFmt.tm_hour)
        else:
            hour = str(timeStampFmt.tm_hour)
        
        if (int(timeStampFmt.tm_min)/10 == 0):
            minutes = "0"+str(timeStampFmt.tm_min)
        else:
            minutes = str(timeStampFmt.tm_min)
        
        if (int(timeStampFmt.tm_sec)/10 == 0):
            sec = "0"+str(timeStampFmt.tm_sec)
        else:
            sec = str(timeStampFmt.tm_sec)
        
        timeStampInDb = str(timeStampFmt.tm_year)+"-"+str(timeStampFmt.tm_mon)+"-"+str(timeStampFmt.tm_mday)+" "+hour+":"+minutes+":"+sec
        print timeStampInDb
        
        percent = 0
        print fillLevel
        if (fillLevel > 0):
            percent = int(((capacity-int(fillLevel))*100)/capacity)
     
        cursor = db.cursor()
    
        sqlQuery = "select f.filllevel,f.TimeStamp,s.BinId from smartbin s inner join smartbinfilllevel f  on f.BinId = s.BinId where s.BinId=52 AND f.isValid = 1"
        try:
            cursor.execute(sqlQuery)
            results = cursor.fetchall()
        except:
            print "Error: Unable to fetch data"
        
        for rows in results:
            validFillLevel = rows[0]

        sqlQuery = "select f.filllevel,f.TimeStamp,s.BinId from smartbin s inner join smartbinfilllevel f  on f.BinId = s.BinId where s.BinId=52 AND f.TimeStamp LIKE '"+timeStampInDb+"'"
        print sqlQuery
    
        try:
            cursor.execute(sqlQuery)
            results = cursor.fetchall()
        except:
            print "Error: Unable to fetch data"
                    
        if (len(results) == 0):    
            if (validFillLevel == percent):
                continue

            sqlQuery = "UPDATE `smartbin_db1`.`smartbinfilllevel` SET `isValid` = '0' WHERE `smartbinfilllevel`.`BinId` = 52; "
            try:
                print sqlQuery
                cursor.execute(sqlQuery)
                db.commit()
            except:
                print "Error: Unable to update data"
            db.rollback()
            
            sqlQuery = "INSERT INTO `smartbin_db1`.`smartbinfilllevel` (`BinId`, `FillLevel`, `TimeStamp`,`isValid` ) VALUES (52, '"+str(percent)+"', '"+timeStampInDb+"', 1)"
            print sqlQuery
            try:
                cursor.execute(sqlQuery)
                db.commit()
            except:
                print "Error: Unable to insert data"
                db.rollback()
        else:
            continue

    closeBinDb(db)

# Shutdown your cron thread if the web process is stopped
atexit.register(lambda: cron.shutdown(wait=False))

def connectBinDb():
    hostname = "127.0.0.1"
    username = "root"
    password = "password"
    dbName = "smartbin_db1"
    
    db = MySQLdb.connect(hostname,username,password, dbName)
    
    return db

def closeBinDb(db):
    db.close()


if __name__ == '__main__':
    app.run()