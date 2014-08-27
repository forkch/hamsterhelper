'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;


var HamstergroupSchema = new Schema({
  name: String,
  hamsters: [{type: Schema.ObjectId, ref: 'Hamster'}]
});

module.exports = mongoose.model('Hamstergroup', HamstergroupSchema);