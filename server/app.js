const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");

const user = require("./routes/user");
const profile = require("./routes/project");

const app = express();

/** Load Models */
const User = require("./models/User");

/** Body Parser Middleware */
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

/** DB Config */
const db = require("./config/keys").databaseUri;

/** Connect to MongoDB */
mongoose
  .connect(db, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log("MongoDB Connected"))
  .catch(err => console.log(err));

/** Use routes */
app.use("/user", user);

const port = process.env.PORT || 3000;

app.listen(port, () => console.log(`Server running on port ${port}`));
