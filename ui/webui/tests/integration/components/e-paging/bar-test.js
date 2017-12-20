import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-paging/bar', 'Integration | Component | e paging/bar', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-paging/bar}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-paging/bar}}
      template block text
    {{/e-paging/bar}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
