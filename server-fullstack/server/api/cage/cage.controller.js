'use strict';

var _ = require('lodash');
var Cage = require('./cage.model');

// Get list of cages
exports.index = function(req, res) {
  Cage.find().populate().exec(function (err, cages) {
    if(err) { return handleError(res, err); }
    return res.json(200, cages);
  });
};

// Get a single cage
exports.show = function(req, res) {
  Cage.findById(req.params.id, function (err, cage) {
    if(err) { return handleError(res, err); }
    if(!cage) { return res.send(404); }
    return res.json(cage);
  });
};

// Creates a new cage in the DB.
exports.create = function(req, res) {
  Cage.create(req.body, function(err, cage) {
    if(err) { return handleError(res, err); }
    return res.json(201, cage);
  });
};

// Updates an existing cage in the DB.
exports.update = function(req, res) {
  if(req.body._id) { delete req.body._id; }
  Cage.findById(req.params.id, function (err, cage) {
    if (err) { return handleError(res, err); }
    if(!cage) { return res.send(404); }
    var updated = _.merge(cage, req.body);
    updated.save(function (err) {
      if (err) { return handleError(res, err); }
      return res.json(200, cage);
    });
  });
};

// Deletes a cage from the DB.
exports.destroy = function(req, res) {
  Cage.findById(req.params.id, function (err, cage) {
    if(err) { return handleError(res, err); }
    if(!cage) { return res.send(404); }
    cage.remove(function(err) {
      if(err) { return handleError(res, err); }
      return res.send(204);
    });
  });
};

function handleError(res, err) {
  return res.send(500, err);
}