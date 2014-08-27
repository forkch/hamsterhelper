'use strict';

var _ = require('lodash');
var Hamster = require('./hamster.model');

// Get list of hamsters
exports.index = function(req, res) {
  Hamster.find(function (err, hamsters) {
    if(err) { return handleError(res, err); }
    return res.json(200, hamsters);
  });
};

// Get a single hamster
exports.show = function(req, res) {
  Hamster.findById(req.params.id, function (err, hamster) {
    if(err) { return handleError(res, err); }
    if(!hamster) { return res.send(404); }
    return res.json(hamster);
  });
};

// Creates a new hamster in the DB.
exports.create = function(req, res) {
  Hamster.create(req.body, function(err, hamster) {
    if(err) { return handleError(res, err); }
    return res.json(201, hamster);
  });
};

// Updates an existing hamster in the DB.
exports.update = function(req, res) {
  if(req.body._id) { delete req.body._id; }
  Hamster.findById(req.params.id, function (err, hamster) {
    if (err) { return handleError(res, err); }
    if(!hamster) { return res.send(404); }
    var updated = _.merge(hamster, req.body);
    updated.save(function (err) {
      if (err) { return handleError(res, err); }
      return res.json(200, hamster);
    });
  });
};

// Deletes a hamster from the DB.
exports.destroy = function(req, res) {
  Hamster.findById(req.params.id, function (err, hamster) {
    if(err) { return handleError(res, err); }
    if(!hamster) { return res.send(404); }
    hamster.remove(function(err) {
      if(err) { return handleError(res, err); }
      return res.send(204);
    });
  });
};

function handleError(res, err) {
  return res.send(500, err);
}