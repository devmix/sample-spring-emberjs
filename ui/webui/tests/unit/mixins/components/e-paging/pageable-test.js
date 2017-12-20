import Ember from 'ember';
import ComponentsEPagingPageableMixin from 'webui/mixins/components/e-paging/pageable';
import { module, test } from 'qunit';

module('Unit | Mixin | components/e paging/pageable');

// Replace this with your real tests.
test('it works', function(assert) {
  let ComponentsEPagingPageableObject = Ember.Object.extend(ComponentsEPagingPageableMixin);
  let subject = ComponentsEPagingPageableObject.create();
  assert.ok(subject);
});
