import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('elements/paging-bar/size', 'Integration | Component | elements/paging bar/size', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{elements/paging-bar/size}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#elements/paging-bar/size}}
      template block text
    {{/elements/paging-bar/size}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
