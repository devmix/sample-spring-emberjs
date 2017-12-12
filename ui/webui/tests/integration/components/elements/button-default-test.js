import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('elements/button-default', 'Integration | Component | elements/button default', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{elements/button-default}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#elements/button-default}}
      template block text
    {{/elements/button-default}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
