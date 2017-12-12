import Ember from 'ember';
import ComponentsBaseMixin from 'webui/mixins/components/base';
import { module, test } from 'qunit';

module('Unit | Mixin | components/base');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsBaseObject = Ember.Object.extend(ComponentsBaseMixin);
  let subject = ComponentsBaseObject.create();
  assert.ok(subject);
});
