'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var CageSchema = new Schema({
  name: String,
  info: String,
  hamsters: [{type: Schema.ObjectId, ref: 'Hamster'}],
  qrCode: String
});

module.exports = mongoose.model('Cage', CageSchema);