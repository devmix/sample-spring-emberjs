import Ember from 'ember';
import ComponentsESearchingSearchableMixin from 'webui/mixins/components/e-searching/searchable';
import { module, test } from 'qunit';

module('Unit | Mixin | components/e searching/searchable');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsESearchingSearchableObject = Ember.Object.extend(ComponentsESearchingSearchableMixin);
  let subject = ComponentsESearchingSearchableObject.create();
  assert.ok(subject);
});
