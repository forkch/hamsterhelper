'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var HamsterSchema = new Schema({
  name: String,
  male: Boolean,
  gencode: String,
  hamsterImage: String,
  birthday: Date,
  motherId: {type: Schema.ObjectId, ref: 'Hamster'},
  fatherId: {type: Schema.ObjectId, ref: 'Hamster'}
});

module.exports = mongoose.model('Hamster', HamsterSchema);