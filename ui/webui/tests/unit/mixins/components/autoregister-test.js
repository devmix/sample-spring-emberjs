import Ember from 'ember';
import ComponentsAutoregisterMixin from 'webui/mixins/components/autoregister';
import { module, test } from 'qunit';

module('Unit | Mixin | components/autoregister');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsAutoregisterObject = Ember.Object.extend(ComponentsAutoregisterMixin);
  let subject = ComponentsAutoregisterObject.create();
  assert.ok(subject);
});
