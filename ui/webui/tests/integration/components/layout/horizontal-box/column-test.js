import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('layout/horizontal-box/column', 'Integration | Component | layout/horizontal box/column', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{layout/horizontal-box/column}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#layout/horizontal-box/column}}
      template block text
    {{/layout/horizontal-box/column}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
