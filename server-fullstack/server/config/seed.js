/**
 * Populate DB with sample data on server start
 * to disable, edit config/environment/index.js, and set `seedDB: false`
 */

'use strict';

var User = require('../api/user/user.model');
var Hamster = require('../api/hamster/hamster.model');
var HamsterGroup = require('../api/hamstergroup/hamstergroup.model');

var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

User.find({}).remove(function () {
  User.create({
      provider: 'local',
      name: 'Test User',
      email: 'test@test.com',
      password: 'test'
    }, {
      provider: 'local',
      role: 'admin',
      name: 'Admin',
      email: 'admin@admin.com',
      password: 'admin'
    }, function () {
      console.log('finished populating users');
    }
  );
});

var idBinu = mongoose.Types.ObjectId();
var idOnyx = mongoose.Types.ObjectId();
Hamster.find({}).remove(function () {
  Hamster.create({
    _id: idOnyx,
    name: 'Onyx',
    male: false,
    birthday: new Date
  }, {
    _id: idBinu,
    name: 'Binu',
    male: true,
    birthday: new Date
  }, {
    name: 'Joe',
    male: true,
    birthday: new Date,
    motherId: idOnyx,
    fatherId: idBinu
  }, {
    name: 'Devon',
    male: true,
    birthday: new Date
  }, {
    name: 'Athena',
    male: false,
    birthday: new Date
  }, {
    name: 'Mio',
    male: true,
    birthday: new Date
  }, {
    name: 'Pipi',
    male: false,
    birthday: new Date
  }, {
    name: 'Yasur',
    male: true,
    birthday: new Date
  }, function () {
    console.log('finished populating hamsters');

    HamsterGroup.find({}).remove(function () {
      HamsterGroup.create({
        name: 'Ben'
      }, {
        name: 'Sabrina'
      }, function () {
        console.log('finished populating hamstergroups');
      })
    });
  })
});
