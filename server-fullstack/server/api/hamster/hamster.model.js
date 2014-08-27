'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var HamsterSchema = new Schema({
  name: String,
  info: String,
  active: Boolean
});

module.exports = mongoose.model('Hamster', HamsterSchema);