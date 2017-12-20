import Ember from 'ember';
import ComponentsStyleableMixin from 'webui/mixins/components/styleable';
import { module, test } from 'qunit';

module('Unit | Mixin | components/styleable');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsStyleableObject = Ember.Object.extend(ComponentsStyleableMixin);
  let subject = ComponentsStyleableObject.create();
  assert.ok(subject);
});
