import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('form/fields-container', 'Integration | Component | form/fields container', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{form/fields-container}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#form/fields-container}}
      template block text
    {{/form/fields-container}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
