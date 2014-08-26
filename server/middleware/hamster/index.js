
'use strict';

/**
 * Routes
 */
var Hamster = require('../../models/hamster.js');


var routes = [];

/**
 * GET /hamster
 * Version: 1.0.0
 */
routes.push({
  meta: {
    name: 'getHamsters',
    method: 'GET',
    paths: [
      '/hamster'
    ],
    version: '1.0.0'
  },
  middleware: function( req, res, next ) {
      Hamster.find(function(err, hamsters) {
          res.send(hamsters);
      })
    return next();
  }
});

/**
 * GET /hamster/:id
 * Version: 1.0.0
 */
routes.push({
  meta: {
    name: 'getHamsterWithId',
    method: 'GET',
    paths: [
      '/hamster/:id'
    ],
    version: '1.0.0'
  },
  middleware: function( req, res, next ) {
      Hamster.findById(req.params.id, function(err, hamster) {
          if(!err) {
              res.send(hamster);
          } else {
              res.send(err);
          }
          return next();
      });
  }
});

/**
 * POST /hamster
 * Version: 1.0.0
 */
routes.push({
    meta: {
        name: 'postHamsters',
        method: 'POST',
        paths: [
            '/hamster'
        ],
        version: '1.0.0'
    },
    middleware: function( req, res, next ) {
        var h = new Hamster(req.params);
        h.save(function(err, savedHamster) {
            console.log(savedHamster);
            if(err) {
                res.send(err);
            }else {
                res.send(savedHamster);
            }
        });
        return next();
    }
});

/**
 * DEL /hamster
 * Version: 1.0.0
 */
routes.push({
    meta: {
        name: 'deleteHamsters',
        method: 'DEL',
        paths: [
            '/hamster'
        ],
        version: '1.0.0'
    },
    middleware: function( req, res, next ) {
        Hamster.remove(function(err) {
            if(err) {
                res.send(err);
            }else {
                res.send();
            }
        });
        return next();
    }
});

/**
 * DEL /hamster/:id
 * Version: 1.0.0
 */
routes.push({
    meta: {
        name: 'deleteHamstersWithId',
        method: 'DEL',
        paths: [
            '/hamster/:id'
        ],
        version: '1.0.0'
    },
    middleware: function( req, res, next ) {
        Hamster.remove({_id: req.params.id}, function(err) {
            if(err) {
                res.send(err);
            }else {
                res.send();
            }
        });
        return next();
    }
});


/**
 * Export
 */

module.exports = routes;
