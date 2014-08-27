'use strict';

var _ = require('lodash');
var Hamstergroup = require('./hamstergroup.model');

// Get list of hamstergroups
exports.index = function (req, res) {
  Hamstergroup.find().populate('hamsters').exec(function (err, hamstergroups) {
    if (err) {
      return handleError(res, err);
    }
    return res.json(200, hamstergroups);
  });
};

// Get a single hamstergroup
exports.show = function (req, res) {
  Hamstergroup.findById(req.params.id, function (err, hamstergroup) {
    if (err) {
      return handleError(res, err);
    }
    if (!hamstergroup) {
      return res.send(404);
    }
    return res.json(hamstergroup);
  });
};

// Creates a new hamstergroup in the DB.
exports.create = function (req, res) {
  Hamstergroup.create(req.body, function (err, hamstergroup) {
    if (err) {
      return handleError(res, err);
    }
    return res.json(201, hamstergroup);
  });
};

// Updates an existing hamstergroup in the DB.
exports.update = function (req, res) {
  if (req.body._id) {
    delete req.body._id;
  }
  Hamstergroup.findById(req.params.id, function (err, hamstergroup) {
    if (err) {
      return handleError(res, err);
    }
    if (!hamstergroup) {
      return res.send(404);
    }
    var updated = _.merge(hamstergroup, req.body);
    updated.save(function (err) {
      if (err) {
        return handleError(res, err);
      }
      return res.json(200, hamstergroup);
    });
  });
};

// Deletes a hamstergroup from the DB.
exports.destroy = function (req, res) {
  Hamstergroup.findById(req.params.id, function (err, hamstergroup) {
    if (err) {
      return handleError(res, err);
    }
    if (!hamstergroup) {
      return res.send(404);
    }
    hamstergroup.remove(function (err) {
      if (err) {
        return handleError(res, err);
      }
      return res.send(204);
    });
  });
};

function handleError(res, err) {
  return res.send(500, err);
}