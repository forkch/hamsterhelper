/**
 * Broadcast updates to client when the model changes
 */

'use strict';

var Hamster = require('./hamster.model');

exports.register = function(socket) {
  Hamster.schema.post('save', function (doc) {
    onSave(socket, doc);
  });
  Hamster.schema.post('remove', function (doc) {
    onRemove(socket, doc);
  });
}

function onSave(socket, doc, cb) {
  socket.emit('hamster:save', doc);
}

function onRemove(socket, doc, cb) {
  socket.emit('hamster:remove', doc);
}