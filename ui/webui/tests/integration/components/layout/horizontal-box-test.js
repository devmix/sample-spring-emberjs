import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('layout/horizontal-box', 'Integration | Component | layout/horizontal box', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{layout/horizontal-box}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#layout/horizontal-box}}
      template block text
    {{/layout/horizontal-box}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
