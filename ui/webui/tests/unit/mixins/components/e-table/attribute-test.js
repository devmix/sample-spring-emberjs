import EmberObject from '@ember/object';
import ComponentsETableAttributeMixin from 'webui/mixins/components/e-table/attribute';
import {module, test} from 'qunit';

module('Unit | Mixin | components/e table/attribute');

// Replace this with your real tests.
test('it works', function (assert) {
  let ComponentsETableAttributeObject = EmberObject.extend(ComponentsETableAttributeMixin);
  let subject = ComponentsETableAttributeObject.create();
  assert.ok(subject);
});
