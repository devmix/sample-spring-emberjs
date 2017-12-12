import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('elements/loading-indicator', 'Integration | Component | elements/loading indicator', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{elements/loading-indicator}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#elements/loading-indicator}}
      template block text
    {{/elements/loading-indicator}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
