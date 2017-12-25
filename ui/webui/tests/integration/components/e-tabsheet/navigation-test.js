import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-tabsheet/navigation', 'Integration | Component | e tabsheet/navigation', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-tabsheet/navigation}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-tabsheet/navigation}}
      template block text
    {{/e-tabsheet/navigation}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
