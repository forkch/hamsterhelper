/**
 * Populate DB with sample data on server start
 * to disable, edit config/environment/index.js, and set `seedDB: false`
 */

'use strict';

var User = require('../api/user/user.model');
var Hamster = require('../api/hamster/hamster.model');
var HamsterGroup = require('../api/hamstergroup/hamstergroup.model');


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

Hamster.find({}).remove(function () {
  Hamster.create({
    name: 'Joe',
    male: true,
    birthday: new Date
  }, {
    name: 'Devon',
    male: true,
    birthday: new Date
  }, {
    name: 'Athena',
    male: false,
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
