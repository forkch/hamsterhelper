'use strict';

var mongoose = require('mongoose')
    ,Schema = mongoose.Schema
    ,ObjectId = Schema.ObjectId;

var hamsterSchema = new Schema({
    name: {type: String, required: true},
    male: {type: Boolean},
    gencode: {type: String},
    color: {type: String},
    birthday: {type: Date}



});

module.exports = mongoose.model('hamster', hamsterSchema);