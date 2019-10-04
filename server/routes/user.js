const express = require("express");
const bcrypt = require("bcryptjs");
const isEmpty = require("../validation/is-empty");
const router = express.Router();

let regexEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

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
    isEmpty(req.body.name) ||
    isEmpty(req.body.email) ||
    isEmpty(req.body.password) ||
    isEmpty(req.body.confirmPassword)
  ) {
    if (isEmpty(req.body.name)) {
      errors.name = "Name is required";
    }
    if (isEmpty(req.body.email)) {
      errors.email = "Email is required";
    }
    /**if (!regexEmail.test(req.body.email)) {
      errors.email = "Invalid Email Address";
    }*/
    if (isEmpty(req.body.password)) {
      errors.password = "Password is required";
    }
    if (isEmpty(req.body.confirmPassword)) {
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

  if (isEmpty(req.body.email) || isEmpty(req.body.password)) {
    if (isEmpty(req.body.email)) {
      errors.email = "Email is required";
    } /**else {
      if (!regexEmail.match(req.body.email)) {
        errors.email = "Invalid Email Address";
      }
    }*/
    if (isEmpty(req.body.password)) {
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
