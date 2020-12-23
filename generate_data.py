import csv
from faker import Faker
import datetime
import random

def datagenerate(records, headers):
    fake = Faker('en_US')
    
    roles = ["ADMIN", "USER", "DEVELOPER"]

    with open("customer_data.csv", 'wt') as csvFile:
        writer = csv.DictWriter(csvFile, fieldnames=headers)
        writer.writeheader()
        for _ in range(records):
            writer.writerow({
               "id": fake.uuid4(),
               "firstName" : fake.first_name(),
               "lastName" : fake.last_name(),
               "role": random.choice(roles),
               "createdAt": fake.date_this_decade(),
               "updatedAt" : fake.date_this_decade(),
               "isLoggedIn": False,
               "accessToken": ""
               })

if __name__ == '__main__':
    records = 1000
    headers = ["id", "firstName", "lastName", "role", "createdAt", "updatedAt", "isLoggedIn", "accessToken"]
    datagenerate(records, headers)
    print("CSV generation complete!")
