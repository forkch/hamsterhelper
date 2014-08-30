'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var HamsterSchema = new Schema({
  name: String,
  male: Boolean,
  gencode: String,

});

module.exports = mongoose.model('Hamster', HamsterSchema);