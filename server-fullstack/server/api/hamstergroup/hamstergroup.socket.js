/**
 * Broadcast updates to client when the model changes
 */

'use strict';

var Hamstergroup = require('./hamstergroup.model');

exports.register = function(socket) {
  Hamstergroup.schema.post('save', function (doc) {
    onSave(socket, doc);
  });
  Hamstergroup.schema.post('remove', function (doc) {
    onRemove(socket, doc);
  });
}

function onSave(socket, doc, cb) {
  socket.emit('hamstergroup:save', doc);
}

function onRemove(socket, doc, cb) {
  socket.emit('hamstergroup:remove', doc);
}