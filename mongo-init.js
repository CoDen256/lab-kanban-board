db.createUser({
    user: "kanban",
    pwd: "kanban",
    roles: [{
        role: "readWrite",
        db: "kanban"
    }]
});