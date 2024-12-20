db = new Mongo().getDB("testDB");

db.createCollection('users', { capped: false });
db.createCollection('tests', { capped: false });

db.tests.insert([
    { "item": 1 },
    { "item": 2 },
    { "item": 3 },
    { "item": 4 },
    { "item": 5 }
]);