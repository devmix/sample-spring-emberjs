import Ember from 'ember';
import ComponentsEDialogModalFrameOwnerMixin from 'webui/mixins/components/e-dialog/modal-frame/owner';
import { module, test } from 'qunit';

module('Unit | Mixin | components/e dialog/modal frame/owner');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsEDialogModalFrameOwnerObject = Ember.Object.extend(ComponentsEDialogModalFrameOwnerMixin);
  let subject = ComponentsEDialogModalFrameOwnerObject.create();
  assert.ok(subject);
});
