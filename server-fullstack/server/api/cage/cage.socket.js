/**
 * Broadcast updates to client when the model changes
 */

'use strict';

var Cage = require('./cage.model');

exports.register = function(socket) {
  Cage.schema.post('save', function (doc) {
    onSave(socket, doc);
  });
  Cage.schema.post('remove', function (doc) {
    onRemove(socket, doc);
  });
}

function onSave(socket, doc, cb) {
  socket.emit('cage:save', doc);
}

function onRemove(socket, doc, cb) {
  socket.emit('cage:remove', doc);
}