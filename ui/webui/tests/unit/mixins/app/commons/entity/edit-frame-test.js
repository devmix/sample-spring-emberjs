import Ember from 'ember';
import AppCommonsEntityEditFrameMixin from 'webui/mixins/app/commons/entity/edit-frame';
import {module, test} from 'qunit';

module('Unit | Mixin | app/commons/entity/edit frame');

// Replace this with your real tests.
test('it works', function (assert) {
  let AppCommonsEntityEditFrameObject = Ember.Object.extend(AppCommonsEntityEditFrameMixin);
  let subject = AppCommonsEntityEditFrameObject.create();
  assert.ok(subject);
});
