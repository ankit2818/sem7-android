const express = require("express");
const bcrypt = require("bcryptjs");
const router = express.Router();

/** Load Model */
const User = require("../models/User");

router.post("/register", (req, res) => {
  const errors = {
    status: false,
    name: "",
    email: "",
    password: "",
    confirmPassword: ""
  };
  const response = {
    status: false,
    email: ""
  };
  if (
    req.body.name == "" ||
    req.body.email == "" ||
    req.body.password == "" ||
    req.body.confirmPassword == ""
  ) {
    if (req.body.name == "") {
      errors.name = "Name is required";
    }
    if (req.body.email == "") {
      errors.email = "Email is required";
    }
    if (req.body.password == "") {
      errors.password = "Password is required";
    }
    if (req.body.confirmPassword == "") {
      errors.confirmPassword = "Confirm Password is required";
    }
    return res.json(errors);
  }
  if (req.body.password != req.body.confirmPassword) {
    errors.confirmPassword = "Password didn't match";
    return res.json(errors);
  }
  User.findOne({ email: req.body.email }).then(user => {
    if (user) {
      errors.email = "User with this email already exists";
      return res.json(errors);
    } else {
      const newUser = new User({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password
      });
      newUser
        .save()
        .then(user => {
          response.status = true;
          response.email = req.body.email;
          return res.json(response);
        })
        .catch(err => console.log(err));
    }
  });
});

router.post("/login", (req, res) => {
  const errors = {
    loggedIn: false,
    email: "",
    password: ""
  };
  const response = {
    email: "",
    token: "",
    loggedIn: false
  };

  if (req.body.email == "" || req.body.password == "") {
    if (req.body.email == "") {
      errors.email = "Email is required";
    }
    if (req.body.password == "") {
      errors.password = "Password is required";
    }
    return res.json(errors);
  }
  const email = req.body.email;
  const password = req.body.password;
  /** Find user by email */
  User.findOne({ email }).then(user => {
    /** Check for user */
    if (!user) {
      errors.email = "User not found";
      return res.json(errors);
    }
    /** Check password */
    if (user.password == password) {
      response.loggedIn = true;
      response.email = user.email;
      response.token = "testToken";
      return res.json(response);
    } else {
      errors.password = "Wrong Password";
      return res.json(errors);
    }
  });
});

module.exports = router;
