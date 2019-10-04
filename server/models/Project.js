const mongoose = require("mongoose");
const Schema = mongoose.Schema;

/** Create Project Schema */
const ProjectSchema = new Schema({
  name: {
    type: String,
    required: true
  },
  createdBy: {
    type: String,
    required: true
  },
  date: {
    type: Date,
    default: Date.now
  },
  description: {
    type: String,
    required: true
  }
  // members: [
  //   {
  //     member: {
  //       type: Schema.Types.ObjectId,
  //       ref: "users"
  //     }
  //   }
  // ]
});

module.exports = Project = mongoose.model("projects", ProjectSchema);
