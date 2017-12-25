import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-tabsheet/standard', 'Integration | Component | e tabsheet/standard', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-tabsheet/standard}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-tabsheet/standard}}
      template block text
    {{/e-tabsheet/standard}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
