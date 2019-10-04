const express = require("express");
const isEmpty = require("../validation/is-empty");
const router = express.Router();

/** Load Model */
const User = require("../models/User");
const Project = require("../models/Project");

router.post("/create", (req, res) => {
  const response = {
    success: false,
    projectNameError: "",
    descriptionError: "",
    membersError: ""
  };
  if (isEmpty(req.body.projectName)) {
    response.projectNameError = "Project Name is Required";
  }
  if (isEmpty(req.body.description)) {
    response.descriptionError = "Description is Required";
  }
  if (isEmpty(req.body.members)) {
    response.membersError = "Add atleast 1 Member";
  }

  console.log(req.body);

  const newProject = new Project({
    name: req.body.projectName,
    createdBy: "testName",
    description: req.body.description,
    members: req.body.members
  });

  newProject
    .save()
    .then(project => {
      response.success = true;
      return res.json(response);
    })
    .catch(err => {
      response.success = true;
      return res.json(response);
    });
});

module.exports = router;